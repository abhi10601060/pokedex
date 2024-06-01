package com.example.pokedex.model.category


import com.google.gson.annotations.SerializedName

data class CategoryOutput(
    @SerializedName("damage_relations")
    val damageRelations: DamageRelations,
    @SerializedName("game_indices")
    val gameIndices: List<GameIndice>,
    @SerializedName("generation")
    val generation: GenerationX,
    @SerializedName("id")
    val id: Int,
    @SerializedName("move_damage_class")
    val moveDamageClass: MoveDamageClass,
    @SerializedName("moves")
    val moves: List<Move>,
    @SerializedName("name")
    val name: String,
    @SerializedName("names")
    val names: List<Name>,
    @SerializedName("past_damage_relations")
    val pastDamageRelations: List<PastDamageRelation>,
    @SerializedName("pokemon")
    val pokemon: List<PokemonFromCategory>
)