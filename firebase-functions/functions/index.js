/* eslint-disable promise/always-return,promise/catch-or-return */
const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);


getDatabase().settings = {timestampsInSnapshots: true};
// exports.gameWritten = functions.firestore.document("games/{gameId}").onWrite((change, context) => {
//     const document = change.after.data();
//     if (document === undefined) {
//         console.log('gameWriteDeleted', context.params.gameId, document);
//         return null;
//     }
//
//     console.log('gameWrite', context.params.gameId, document);
//     return context.params.gameId
// });

exports.DeleteGameFunction = functions.firestore
    .document("games/{gameId}")
    .onDelete((snap, context) => {
        // Get an object representing the document prior to deletion
        const gameId = context.params.gameId;

        const BATCH_SIZE = 500;

        const database = getDatabase();

        const usersInGame = getGameCollection().doc(gameId).collection("users");
        const statsInGame = getGameCollection().doc(gameId).collection("stats");
        const classesInGame = getGameCollection().doc(gameId).collection("classes");
        const racesInGame = getGameCollection().doc(gameId).collection("races");
        const tagsInGame = getGameCollection().doc(gameId).collection("tags");
        const photosInGame = getGameCollection().doc(gameId).collection("photos");

        const deleteGameInUser =
            new Promise(((resolve, reject) => {
                usersInGame.get()
                    .then((querySnapshot) => {
                        querySnapshot.forEach((doc) => {
                            console.log(doc.id, " => ", doc.data());

                            getUsersCollection().doc(doc.id).collection("games").doc(gameId).delete();
                            const collection = usersInGame.doc(doc.id).collection("dices");

                            deleteQueryBatch(getDatabase(), collection, 500, resolve, reject);
                        });
                    })
                    .catch((error) => {
                        console.log("Error getting documents: ", error);
                    });
            }));

        const deletePhotos = new Promise(((resolve, reject) => {
            photosInGame.get().then((querySnapshot) => {
                querySnapshot.forEach((doc) => {
                        getPhotosInGameReference(gameId, doc.id).delete()
                            .catch((error) => {
                                console.log("Error getting documents: ", error);
                            });
                    }
                );
                deleteQueryBatch(getDatabase(), photosInGame, 500, resolve, reject);
            }).catch((error) => {
                console.log("Error getting documents: ", error);
            });
        })).catch((error) => {
            console.log("Error getting documents: ", error);
        });

        const deleteUsersInGame = deleteCollection(database, usersInGame, BATCH_SIZE);
        const deleteStatsInGame = deleteCollection(database, statsInGame, BATCH_SIZE);
        const deleteClassesInGame = deleteCollection(database, classesInGame, BATCH_SIZE);
        const deleteRacesInGame = deleteCollection(database, racesInGame, BATCH_SIZE);
        const deleteTagsInGame = deleteCollection(database, tagsInGame, BATCH_SIZE);

        return Promise.all([deletePhotos, deleteGameInUser,
            deleteUsersInGame, deleteStatsInGame,
            deleteClassesInGame, deleteRacesInGame,
            deleteTagsInGame
        ]);
    });

function getPhotosInGameReference(gameId, photoId) {
    return getStorage().bucket("rpsupdated.appspot.com").file("games/" + gameId + "/photos/" + photoId);
}

function getGameCollection() {
    return getDatabase().collection('games');
}

function getUsersCollection() {
    return getDatabase().collection('users');
}

function getDatabase() {
    return admin.firestore();
}

function getStorage() {
    return admin.storage();
}

/**
 * Delete a collection, in batches of batchSize. Note that this does
 * not recursively delete subcollections of documents in the collection
 */
function deleteCollection(db, collectionRef, batchSize) {
    var query = collectionRef.orderBy('__name__').limit(batchSize);

    return new Promise(((resolve, reject) => {
        deleteQueryBatch(db, query, batchSize, resolve, reject)
    }))
}

function deleteQueryBatch(db, query, batchSize, resolve, reject) {
    query.get()
        .then((snapshot) => {
            // When there are no documents left, we are done
            if (snapshot.size === 0) {
                return 0
            }

            // Delete documents in a batch
            var batch = db.batch();
            snapshot.docs.forEach((doc) => {
                batch.delete(doc.ref)
            });

            return batch.commit().then(() => {
                return snapshot.size
            })
        }).then((numDeleted) => {
        if (numDeleted <= batchSize) {
            resolve();
        }
        else {
            // Recurse on the next process tick, to avoid
            // exploding the stack.
            return process.nextTick(() => {
                deleteQueryBatch(db, query, batchSize, resolve, reject)
            })
        }
    })
        .catch(reject)
}

const data = require("./fakedb.json");

/**
 * Data is a collection if
 *  - it has a odd depth
 *  - contains only objects or contains no objects.
 */

function isCollection(data, path, depth) {
    if (
        typeof data !== 'object' ||
        data === null ||
        data.length === 0 ||
        isEmpty(data)
    ) {
        return false;
    }

    for (const key in data) {
        if (typeof data[key] !== 'object' || data[key] === null) {
            // If there is at least one non-object item then it data then it cannot be collection.
            return false;
        }
    }

    return true;
}

// Checks if object is empty.
function isEmpty(obj) {
    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            return false;
        }
    }
    return true;
}

function upload(data, path) {
    return admin.firestore()
        .doc(path.join('/'))
        .set(data)
        .then(() => console.log(`Document ${path.join('/')} uploaded.`))
        .catch(() => console.error(`Could not write document ${path.join('/')}.`));
}

function resolve(data, path = []) {
    if (path.length > 0 && path.length % 2 === 0) {
        // Document's length of path is always even, however, one of keys can actually be a collection.

        // Copy an object.
        const documentData = Object.assign({}, data);

        for (const key in data) {
            // Resolve each collection and remove it from document data.
            if (isCollection(data[key], [...path, key])) {
                // Remove a collection from the document data.
                delete documentData[key];
                // Resolve a colleciton.
                resolve(data[key], [...path, key]);
            }
        }

        // If document is empty then it means it only consisted of collections.
        if (!isEmpty(documentData)) {
            // Upload a document free of collections.
            upload(documentData, path);
        }
    } else {
        // Collection's length of is always odd.
        for (const key in data) {
            // Resolve each collection.
            resolve(data[key], [...path, key]);
        }
    }
}

resolve(data);