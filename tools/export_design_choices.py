import json
from pathlib import Path


def export_design_choices():
    docs_path = Path.cwd().joinpath("docs")
    design_choices_input_path = docs_path.joinpath("other/design-choices.json")

    other_output_path = docs_path.joinpath("./.other")
    other_output_path.mkdir(exist_ok=True)

    design_choices_output_path = other_output_path.joinpath("design-choices.md")

    def export_reason(reason_dict: dict[str, str]) -> str:
        reason_lines = [
            "",
            f"### Grund: {reason_dict['type']}",
            "",
            reason_dict["reason"]
        ]
        return "\n".join(reason_lines)

    def export_design_choice(design_choice_dict: dict[str, any]) -> str:
        design_choice_lines = [
            f"## Entscheidung: {design_choice_dict['title']}",
            "",
            design_choice_dict["description"],
            *(export_reason(x) for x in design_choice_dict["reasons"])
        ]

        return "\n".join(design_choice_lines)

    with open(design_choices_input_path, "r") as json_file:
        data = json.load(json_file)
        design_entscheidungen = data["designEntscheidungen"]

        md_file_blocks = [
            "# Design Entscheidungen",
            "",
            "\n***\n\n".join(export_design_choice(design_choice) for design_choice in design_entscheidungen)
        ]

        with open(design_choices_output_path, "w") as req_json_md_file:
            req_json_md_file.write("\n".join(md_file_blocks))


if __name__ == '__main__':
    export_design_choices()
