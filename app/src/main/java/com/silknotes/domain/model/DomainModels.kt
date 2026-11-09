package com.silknotes.domain.model

import java.time.Instant

data class Folder(
    val id: Long,
    val name: String,
    val color: Int,
    val icon: String,
    val sortOrder: Int,
    val isSystem: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val noteCount: Int = 0
)

data class Note(
    val id: Long,
    val folderId: Long,
    val title: String,
    val isPinned: Boolean,
    val pageCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val lastOpenedAt: Instant?
)

data class Page(
    val id: Long,
    val noteId: Long,
    val pageIndex: Int,
    val backgroundColor: Int,
    val strokes: List<Stroke>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class Stroke(
    val points: List<StrokePoint>,
    val color: Int,
    val strokeWidth: Float,
    val toolType: String,
    val pressureSensitive: Boolean
)

data class StrokePoint(
    val x: Float,
    val y: Float,
    val pressure: Float,
    val timestamp: Long
)

enum class ToolType(val id: String, val icon: String, val label: String) {
    PEN("pen", "edit", "Pen"),
    MARKER("marker", "brush", "Marker"),
    ERASER("eraser", "eraser", "Eraser"),
    SELECT("select", "gesture", "Select"),
    TEXT("text", "text_fields", "Text")
}

enum class ThemeMode(val id: Int, val label: String) {
    SYSTEM(0, "Follow System"),
    LIGHT(1, "Light"),
    DARK(2, "Dark")
}

enum class SortOrder(val id: Int, val label: String) {
    DATE_DESC(0, "Date (Newest)"),
    DATE_ASC(1, "Date (Oldest)"),
    TITLE_ASC(2, "Title (A-Z)"),
    TITLE_DESC(3, "Title (Z-A)")
}

enum class ViewMode(val id: Int, val label: String) {
    LIST(0, "List"),
    GRID(1, "Grid")
}

data class NoteWithPages(
    val note: Note,
    val pages: List<Page>
)

data class FolderWithNotes(
    val folder: Folder,
    val notes: List<Note>
)