'use strict';

module.exports = {
    game: class Game {
        constructor() {

        }

        static get dateCreate() {
            return "dateCreate"
        }

        static get masterId() {
            return "masterId"
        }

        static get setting() {
            return "setting"
        }

        static get description() {
            return "description"
        }

        static get password() {
            return "password"
        }

        static get name() {
            return "name"
        }

        static get status() {
            return "status"
        }

        static get masterName() {
            return "masterName"
        }
    },

    gameInUser: class GameInUser {
        constructor() {

        }

        static get dateUpdate() {
            return "dateUpdate"
        }
    }

};