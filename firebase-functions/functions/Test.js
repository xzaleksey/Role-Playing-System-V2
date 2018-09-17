const data = require("./fakedb.json");

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
    console.log("data " + data.toString() + " " + path.join("/"));
    return "data " + data.toString() + "path " + path.join("/")
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