import json
from pathlib import Path


def export_glossaries():
    docs_path = Path.cwd().joinpath("docs")
    glossaries_input_path = docs_path.joinpath("other/glossaries.json")

    other_output_path = docs_path.joinpath("./.other")
    other_output_path.mkdir(exist_ok=True)

    glossaries_output_path = other_output_path.joinpath("glossaries.md")

    def export_glossary(glossary_dict: dict[str, str]) -> str:
        design_choice_lines = [
            f"## {glossary_dict['name']}",
            "",
            glossary_dict["description"]
        ]

        return "\n".join(design_choice_lines)

    with open(glossaries_input_path, "r") as json_file:
        data = json.load(json_file)
        glossaries = data["glossaries"]

        md_file_blocks = [
            "# Glossar",
            "",
            "\n\n***\n\n".join(export_glossary(glossary) for glossary in glossaries),
            ""
        ]

        with open(glossaries_output_path, "w") as req_json_md_file:
            req_json_md_file.write("\n".join(md_file_blocks))


if __name__ == '__main__':
    export_glossaries()
