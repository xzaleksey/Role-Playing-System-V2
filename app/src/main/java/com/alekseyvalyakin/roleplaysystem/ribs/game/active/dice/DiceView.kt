package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import android.content.Context
import org.jetbrains.anko._FrameLayout

/**
 * Top level view for {@link DiceBuilder.DiceScope}.
 */
class DiceView constructor(context: Context) : _FrameLayout(context), DiceInteractor.DicePresenter {

    init {
//        relativeLayout {
//            id = R.id.main_container
//            //tools:appContext = com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view.ParentActivity //not support attribute
//            relativeLayout {
//                id = R.id.top_container
//                backgroundResource = R.drawable.top_background
//                //android:visibility = visible //not support attribute
//                view {
//                    id = R.id.status_bar
//                }.lparams(width = matchParent, height = getIntDimen(R.dimen.status_bar_height))
//                frameLayout {
//                    layoutTransition = LayoutTransition()
//                    bottomPadding = getIntDimen(R.dimen.dp_8)
//                    topPadding = getIntDimen(R.dimen.dp_16)
//
//                    linearLayout {
//                        id = R.id.no_dices_container
//                        orientation = LinearLayout.VERTICAL
//                        leftPadding = getIntDimen(R.dimen.dp_16)
//                        rightPadding = dip(@dimen /)
//                        //android:visibility = gone //not support attribute
//                        //tools:visibility = gone //not support attribute
//                        textView {
//                            id = R.id.tv_title
//                            //fontPath = fonts/Roboto-Medium.ttf //not support attribute
//                            text = resources.getString(R.string.save_dices_title)
//                            textColor = Color.WHITE
//                            textSize = dip(@dimen /).toFloat()
//                            //tools:ignore = MissingPrefix,SpUsage //not support attribute
//                        }.lparams(width = matchParent)
//                        textView {
//                            id = R.id.tv_sub_title
//                            text = resources.getString(R.string.save_dices_secondary)
//                            textColor = resources.getColor(R.color.white7)
//                            textSize = dip(@dimen /).toFloat()
//                            //tools:ignore = SpUsage //not support attribute
//                        }.lparams(width = matchParent) {
//                            topMargin = dip(@dimen /)
//                        }
//                    }.lparams(width = matchParent)
//                    relativeLayout {
//                        id = R.id.dices_collections_container
//                        leftPadding = dip(@dimen /)
//                        textView {
//                            id = R.id.saved_dices_count
//                            //fontPath = fonts/Roboto-Medium.ttf //not support attribute
//                            rightPadding = dip(@dimen /)
//                            textColor = Color.WHITE
//                            textSize = @dimen / f //sp
//                            //tools:ignore = MissingPrefix //not support attribute
//                            //tools:text = Сохраненные наборы (8) //not support attribute
//                        }.lparams(width = matchParent)
//                        recyclerView {
//                            id = R.id.recycler_view_dices_collections
//                            //android:scrollbars = vertical //not support attribute
//                        }.lparams(width = matchParent, height = matchParent) {
//                            below(R.id.saved_dices_count)
//                            topMargin = dip(@dimen /)
//                        }
//                    }.lparams(width = matchParent, height = matchParent)
//                }.lparams(width = matchParent, height = matchParent) {
//                    below(R.id.status_bar)
//                }
//            }.lparams(width = matchParent, height = @dimen / game_characters_top_element_height) {
//                alignParentTop()
//            }
//            button {
//                id = R.id.btn_throw
//                backgroundResource = R.drawable.accent_button
//                text = resources.getString(R.string.throw_dice)
//                textColor = resources.getColor(R.color.material_light_white)
//            }.lparams(width = matchParent) {
//                alignParentBottom()
//                bottomMargin = dip(@dimen /)
//                leftMargin = dip(@dimen /)
//                rightMargin = dip(@dimen /)
//            }
//            relativeLayout {
//                id = R.id.label_container
//                textView {
//                    id = R.id.new_throw
//                    //android:layout_alignParentStart = true //not support attribute
//                    bottomPadding = dip(@dimen /)
//                    topPadding = dip(@dimen /)
//                    text = resources.getString(R.string.new_throw)
//                }.lparams {
//                    alignParentLeft()
//                    leftMargin = dip(@dimen /)
//                    rightMargin = dip(@dimen /)
//                }
//                textView {
//                    id = R.id.save
//                    //fontPath = fonts/Roboto-Medium.ttf //not support attribute
//                    //android:layout_alignParentEnd = true //not support attribute
//                    backgroundColor = Color.parseColor("?attr/selectableItemBackgroundBorderless")
//                    padding = dip(@dimen /)
//                    text = resources.getString(R.string.save_set)
//                    textColor = Color.parseColor("@drawable/textview_enable_text_color")
//                    //tools:ignore = MissingPrefix //not support attribute
//                }.lparams {
//                    alignParentRight()
//                    rightMargin = dip(@dimen /)
//                    topMargin = dip(@dimen /)
//                }
//            }.lparams(width = matchParent) {
//                below(R.id.top_container)
//            }
//            cardView {
//                recyclerView {
//                    id = R.id.recycler_view
//                    backgroundColor = Color.WHITE
//                    //android:scrollbars = vertical //not support attribute
//                }.lparams(width = matchParent) {
//                    above(R.id.btn_throw)
//                    margin = dip(@dimen /)
//                }
//            }.lparams(width = matchParent) {
//                below(R.id.label_container)
//                leftMargin = dip(@dimen /)
//                rightMargin = dip(@dimen /)
//            }
//        }
    }
}
