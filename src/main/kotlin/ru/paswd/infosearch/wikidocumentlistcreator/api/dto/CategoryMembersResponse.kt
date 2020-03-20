package ru.paswd.infosearch.wikidocumentlistcreator.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryMembersResponse (

    @JsonProperty("batchcomplete")
    var batchComplete: Boolean?,

    @JsonProperty("query")
    var query: Query?
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Query (
        @JsonProperty("categorymembers")
        var categoryMembers: List<ShortPage>?
    )
}