package com.jiangkedev

/**
 * @author 姜科 <bazzjiang@hotmail.com>
 * @date 2022-08-30
 * @description
 */
class IslandsDao {

  companion object {
    private val MOCK_ISLANDS by lazy {
      listOf(
        Island("Kotlin", Country("Russia", "RU")),
        Island("Stewart Island", Country("New Zealand", "NZ")),
        Island("Cockatoo Island", Country("Australia", "AU")),
        Island("Tasmania", Country("Australia", "AU"))
      )
    }
  }

  /**
   * Fetches all islands.
   */
  fun fetchIslands() = MOCK_ISLANDS

  /**
   * Fetches countries by code.
   *
   * @param code Optional code to filter countries by.
   */
  fun fetchCountries(code: String? = null) =
    MOCK_ISLANDS.map { it.country }
      .distinct()
      .filter { code == null || it.code.equals(code, true) }
      .sortedBy { it.code }

}
