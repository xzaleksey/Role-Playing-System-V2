package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction

class AllRestrictions(
        val classes: List<RestrictionInfo> = emptyList(),
        val races: List<RestrictionInfo> = emptyList()
) {

    fun getClassesRestrictions(restrictions: List<Restriction>): MutableList<RestrictionInfo> {
        return classes.filter { rinfo ->
            restrictions.contains(rinfo.restriction)
        }.toMutableList()
    }

    fun getAllRestrictions(restrictions: List<Restriction>): MutableList<RestrictionInfo> {
        return (getClassesRestrictions(restrictions) + getRacesRestrictions(restrictions)).toMutableList()
    }

    fun getRacesRestrictions(restrictions: List<Restriction>): MutableList<RestrictionInfo> {
        return races.filter { rinfo ->
            restrictions.contains(rinfo.restriction)
        }.toMutableList()
    }

}