package com.example.pokedex.model.category


import com.google.gson.annotations.SerializedName

data class PastDamageRelation(
    @SerializedName("damage_relations")
    val damageRelations: DamageRelationsX,
    @SerializedName("generation")
    val generation: GenerationX
)