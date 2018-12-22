/* eslint-disable promise/always-return,promise/catch-or-return */
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const classes = require('./classes');
const FieldValue = admin.firestore.FieldValue;
admin.initializeApp(functions.config().firebase);
getDatabase().settings({timestampsInSnapshots: true});

function getDatabase() {
    return admin.firestore();
}

function getStorage() {
    return admin.storage();
}

function getPhotosInGameReference(gameId, photoId) {
    return getStorage().bucket("rpsupdated.appspot.com").file("games/" + gameId + "/photos/" + photoId);
}

function getGameCollection() {
    return getDatabase().collection('games');
}

function getUsersCollection() {
    return getDatabase().collection('users');
}

function getGamesInUsersCollection(userId) {
    return getDatabase().collection('users').doc(userId).collection("games");
}

function getUsersInGameCollection(gameId) {
    return getGameCollection().doc(gameId).collection("users");
}

function getStatsInGameCollection(gameId) {
    return getGameCollection().doc(gameId).collection("stats");
}

function getClassesInGameCollection(gameId) {
    return getGameCollection().doc(gameId).collection("classes");
}

function getRacesInGameCollection(gameId) {
    return getGameCollection().doc(gameId).collection("races");
}

function getTagsInGameCollection(gameId) {
    return getGameCollection().doc(gameId).collection("tags");
}

function getSkillsInGameCollection(gameId) {
    return getGameCollection().doc(gameId).collection("skills");
}

function getPhotosInGameCollection(gameId) {
    return getGameCollection().doc(gameId).collection("photos");
}

exports.tagWritten = functions.firestore.document("games/{gameId}/tags/{tagId}")
    .onWrite((change, context) => {
        const document = change.after.data();
        if (document === undefined) {
            return null;
        }

        console.log('tagWrite', context.params.gameId, document);
        let skills = document["skillIds"];
        if (Array.isArray(skills) && skills.length === 0) {
            let path = "games/" + context.params.gameId + "/tags/" + context.params.tagId;
            console.log(path);
            getDatabase().doc(path).delete();
        }
        return context.params.gameId
    });

function getDeletePhotos(photosInGame, gameId, database) {
    return new Promise(((resolve, reject) => {
        console.log("delete photos in game");
        photosInGame.get().then((querySnapshot) => {
            querySnapshot.forEach((doc) => {
                    getPhotosInGameReference(gameId, doc.id).delete()
                        .catch((error) => {
                            console.log("Error getting documents: ", error);
                        });
                }
            );
        }).then(() => {
            deleteCollection(database, photosInGame, 500)
                .then(() => {
                    console.log("deleted photos");
                    resolve();
                }).catch(reject);
        }).catch((error) => {
            console.log("Error getting documents: ", error);
            reject(error);
        });
    }));
}

exports.DeleteGameFunction = functions.firestore
    .document("games/{gameId}")
    .onDelete((snap, context) => {
        // Get an object representing the document prior to deletion
        const gameId = context.params.gameId;

        const BATCH_SIZE = 500;

        const database = getDatabase();

        const usersInGame = getUsersInGameCollection(gameId);
        const statsInGame = getStatsInGameCollection(gameId);
        const classesInGame = getClassesInGameCollection(gameId);
        const racesInGame = getRacesInGameCollection(gameId);
        const tagsInGame = getTagsInGameCollection(gameId);
        const skillsInGame = getSkillsInGameCollection(gameId);
        const photosInGame = getPhotosInGameCollection(gameId);

        const deleteGameInUser = new Promise(((resolve, reject) => {
            console.log("delete game in user");
            usersInGame.get()
                .then((querySnapshot) => {
                    let promises = [];

                    querySnapshot.forEach((doc) => {
                        console.log(doc.id, " => ", doc.data());
                        getGamesInUsersCollection(doc.id).doc(gameId).delete();
                        const collection = usersInGame.doc(doc.id).collection("dices");

                        promises.push(deleteCollection(getDatabase(), collection, BATCH_SIZE));
                    });

                    return Promise.all(promises)
                        .then(resolve)
                        .catch(reject);
                })
                .catch(reject);
        }));

        const deletePhotos = getDeletePhotos(photosInGame, gameId, database);
        const deleteUsersInGame = deleteCollection(database, usersInGame, BATCH_SIZE);
        const deleteStatsInGame = deleteCollection(database, statsInGame, BATCH_SIZE);
        const deleteClassesInGame = deleteCollection(database, classesInGame, BATCH_SIZE);
        const deleteRacesInGame = deleteCollection(database, racesInGame, BATCH_SIZE);
        const deleteTagsInGame = deleteCollection(database, tagsInGame, BATCH_SIZE);
        const deleteSkillsInGame = deleteCollection(database, skillsInGame, BATCH_SIZE);

        return Promise.all([deletePhotos, deleteGameInUser,
            deleteUsersInGame, deleteStatsInGame,
            deleteClassesInGame, deleteRacesInGame,
            deleteTagsInGame, deleteSkillsInGame
        ]);
    });

exports.copyGame = functions.https.onCall((data, context) => {
    let gameId = data["game_id"];
    let masterName = data["user_name"];
    return new Promise(((resolve, reject) => {
        getDatabase().collection("games").doc(gameId).get().then((value) => {
            let data = value.data();
            if (data === undefined) {
                reject(new functions.https.HttpsError('not-found', "Game not found"));
            } else {
                console.log(data);
                let copiedGame = {};
                Object.assign(copiedGame, data);
                copiedGame[classes.game.dateCreate] = FieldValue.serverTimestamp();
                copiedGame[classes.game.masterId] = context.auth.uid;
                copiedGame[classes.game.masterName] = masterName;
                console.log("before resolve");
                resolve(copiedGame);
            }
        }).catch((error) => {
            console.log(error.toString());
            reject(new functions.https.HttpsError('unknown', error.toString()));
        });
    })).then((copiedGame) => {
        return new Promise((resolve, reject) => {
            getDatabase().collection("games").add(copiedGame).then((docRef) => {
                console.log("Game copied with ID: ", docRef.id);
                resolve(docRef.id);
            }).catch((error) => {
                console.error("Error adding document: ", error);
                reject(new functions.https.HttpsError('internal', "Game was not created"))
            });
        })
    });
});

/**
 * Delete a collection, in batches of batchSize. Note that this does
 * not recursively delete subcollections of documents in the collection
 */
function deleteCollection(db, collectionRef, batchSize) {
    console.log("delete collection " + collectionRef.path);
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

//resolve(data);