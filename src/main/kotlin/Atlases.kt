package cz.lukynka

fun atlasFileContent(name: String): String {
    return "{\n" +
            "  \"sources\": [\n" +
            "    {\n" +
            "      \"type\": \"directory\",\n" +
            "      \"source\": \"$name\",\n" +
            "      \"prefix\": \"$name/\"\n" +
            "    }\n" +
            "  ]\n" +
            "}\n"
}