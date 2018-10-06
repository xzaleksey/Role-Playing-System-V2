package com.alekseyvalyakin.roleplaysystem.data.analytics

//Main
const val CREATE_GAME = "create_game"
const val LOGOUT = "logout"
const val GAME_CLICK = "click_game"
const val PROFILE_CLICK = "click_profile"

//Profile
const val CHANGE_USER_NAME = "change_user_name"
const val UPDATE_AVATAR = "update_avatar"

//GamePhoto
const val OPEN_PHOTO = "open_photo"
const val UPLOAD_PHOTO = "upload_photo"
const val DELETE_PHOTO = "delete_photo"
const val SWITCH_PHOTO_VISIBILITY = "switch_photo_visibility"

//GameDice
const val THROW_DICE = "throw_dice"
const val RETHROW_ALL_DICES = "rethrow_all_dices"
const val RETHROW_DICES = "rethrow_dices"
const val DELETE_DICE_COLLECTION = "delete_dice_collection"
const val CREATE_DICE_COLLECTION = "create_dice_collection"
const val SELECT_DICE_COLLECTION = "select_dice_collection"
const val UNSELECT_DICE_COLLECTION = "unselect_dice_collection"
const val CANCEL_BUTTON_CLICK = "click_cancel_button"

//Common
const val NAVIGATE = "navigate"
const val SKIP = "skip"

//PARAMS
const val PHOTO_ID = "photo_id"
const val VISIBILITY = "visibility"
const val GAME_ID_PARAM = "game_id"
const val STATUS_PARAM = "status"
const val SCREEN_PARAM = "screen"