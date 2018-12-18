package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction

class AllRestrictions(
        val classes: List<RestrictionInfo> = emptyList(),
        val races: List<RestrictionInfo> = emptyList()
) {

    fun getClassesRestrictions(restrictions: List<Restriction>): List<RestrictionInfo> {
        return classes.filter { rinfo ->
            restrictions.contains(rinfo.restriction)
        }
    }

    fun getRacesRestrictions(restrictions: List<Restriction>): List<RestrictionInfo> {
        return races.filter { rinfo ->
            restrictions.contains(rinfo.restriction)
        }
    }

}