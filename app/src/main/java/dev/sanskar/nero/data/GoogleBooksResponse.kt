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
    val description: String = "",
    val imageLinks: ImageLinks = ImageLinks(),
    val industryIdentifiers: List<IndustryIdentifier> = listOf(),
    val language: String,
    val pageCount: Int = -1,
    val publishedDate: String,
    val publisher: String = "",
    val ratingsCount: Int = 0,
    val subtitle: String = "",
    val title: String
)

data class ImageLinks(
    val smallThumbnail: String = "",
    val thumbnail: String = ""
)

data class IndustryIdentifier(
    val identifier: String,
    val type: String
)

fun GoogleBooksVolume.toBook() = Book(
    id,
    volumeInfo.title,
    volumeInfo.subtitle,
    volumeInfo.description,
    volumeInfo.authors,
    volumeInfo.publisher,
    volumeInfo.publishedDate,
    1,
    volumeInfo.pageCount,
    volumeInfo.industryIdentifiers.map { it.identifier },
    volumeInfo.categories,
    "https://" + volumeInfo.imageLinks.thumbnail.removePrefix("http://").removePrefix("https://"),
    volumeInfo.averageRating,
    volumeInfo.ratingsCount
)