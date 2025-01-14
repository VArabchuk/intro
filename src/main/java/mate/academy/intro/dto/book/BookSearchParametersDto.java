package mate.academy.intro.dto.book;

public record BookSearchParametersDto(
        String[] title,
        String[] author,
        String[] isbn
) {
}
