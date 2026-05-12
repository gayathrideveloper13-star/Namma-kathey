package com.mindmatrix.nammakathey

data class HeroData(
    val heroes: List<Hero>
)

data class Hero(
    val id: String,
    val district: String,
    val name_en: String,
    val name_kn: String,
    val image_url: String,
    val story_en: List<String>,
    val story_kn: List<String>,
    val quiz: List<QuizQuestion>
) : java.io.Serializable

data class QuizQuestion(
    val question_en: String,
    val question_kn: String,
    val options_en: List<String>,
    val options_kn: List<String>,
    val correct_index: Int
) : java.io.Serializable
