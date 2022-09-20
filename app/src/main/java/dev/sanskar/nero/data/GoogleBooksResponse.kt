package dev.sanskar.nero.data

data class GoogleBooksResponse(
    val items: List<GoogleBooksVolume>,
    val kind: String,
    val totalItems: Int
)

data class GoogleBooksVolume(
    val id: String,
    val kind: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val authors: List<String>,
    val averageRating: Double = 0.0,
    val categories: List<String> = listOf(),
    val description: String,
    val imageLinks: ImageLinks,
    val industryIdentifiers: List<IndustryIdentifier> = listOf(),
    val language: String,
    val pageCount: Int,
    val publishedDate: String,
    val publisher: String = "",
    val ratingsCount: Int = 0,
    val subtitle: String = "",
    val title: String
)

data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
)

data class IndustryIdentifier(
    val identifier: String,
    val type: String
)