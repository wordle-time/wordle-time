from pathlib import Path
import shutil
import json


def export_design_choices():
    docs_path = Path.cwd().joinpath("docs")
    design_choices_input_path = docs_path.joinpath("other/design-choices.json")

    other_output_path = docs_path.joinpath("./.other")
    other_output_path.mkdir(exist_ok=True)

    design_choices_output_path = other_output_path.joinpath("design-choices.md")

    def export_reason(reason_dict: dict[str, str]) -> str:
        reason_lines = [
            f"### Grund: {reason_dict['type']}",
            reason_dict["reason"]
        ]
        return "\n".join(reason_lines)

    def export_design_choice(design_choice_dict: dict[str, any]) -> str:
        design_choice_lines = [
            f"## Entscheidung: {design_choice_dict['title']}",
            design_choice_dict["description"],
            "",
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

        for design_choice in design_entscheidungen:
            md_file_blocks.append(export_design_choice(design_choice))

        with open(design_choices_output_path, "w") as req_json_md_file:
            req_json_md_file.write("\n".join(md_file_blocks))


if __name__ == '__main__':
    export_design_choices()

#
#
# def testcase_dict_to_md(testcase_dict):
#     test_case_lines = [
#         "",
#         f"### {testcase_dict['name']}",
#         "",
#         f"**Vorbedingung:** {testcase_dict['requirement']}",
#         "",
#         f"**Aktion:** {testcase_dict['action']}",
#         "",
#         f"**Erwartetes Ergebnis:** {testcase_dict['expectation']}",
#         "",
#         f"![{testcase_dict['name']}]({quote(testcase_dict['testPic'])})"
#     ]
#
#     return "\n".join(test_case_lines)
#
#
# def requirement_to_md(json_dict: dict[str, str], add_act_diagram: bool, add_seq_diagram: bool):
#     desc_lines = [
#         f"# {json_dict['id']} - {json_dict['title']}",
#         "",
#         json_dict['description'],
#         "",
#         f"[GitHub-Issue]({json_dict['reference']})",
#         "",
#         "## Auswirkung",
#         "",
#         json_dict['impact'],
#         "",
#         "## Akzeptanzkriterien",
#         "",
#         "\n".join(map(lambda x: f"- {x}", json_dict['criteria'])),
#     ]
#
#     if add_act_diagram:
#         desc_lines.extend([
#             "",
#             "## Aktivitätsdiagramm",
#             "",
#             f"![Aktivitätsdiagramm]({quote(json_dict['id'] + '_act.png')})"
#         ])
#
#     if add_seq_diagram:
#         desc_lines.extend([
#             "",
#             "## Sequenzdiagramm",
#             "",
#             f"![Sequenzdiagramm]({quote(json_dict['id'] + '_seq.png')})"
#         ])
#
#     full_text = "\n".join(desc_lines)
#
#     if json_dict['testCases']:
#         test_cases_lines = [
#             "\n",
#             "## Testfälle"
#         ]
#
#         for testcase_dict in json_dict['testCases']:
#             test_cases_lines.append(testcase_dict_to_md(testcase_dict))
#
#         full_text += "\n".join(test_cases_lines)
#
#     return full_text
#
#
# def handle_requirement_src_path(src_path: Path):
#     dest_path = requirements_output_path.joinpath(src_path.name)
#     dest_path.mkdir(exist_ok=True)
#
#     # Copy PNG/Json
#     for png_path in src_path.glob("*.png"):
#         shutil.copy2(png_path, dest_path.joinpath(png_path.name))
#     for json_path in src_path.glob("*.json"):
#         shutil.copy2(json_path, dest_path.joinpath(json_path.name))
#
#     add_act_diagram = any(True for _ in src_path.glob('*-act.plantuml'))
#     add_seq_diagram = any(True for _ in src_path.glob('*-seq.plantuml'))
#
#     # Generate F**-req.md
#     req_json_path = src_path.joinpath(f"{src_path.name}-req.json")
#     if req_json_path.exists:
#         with open(req_json_path, "r") as json_file:
#             data = json.load(json_file)
#             md_file_content = requirement_to_md(data, add_act_diagram, add_seq_diagram)
#             req_json_md = dest_path.joinpath(req_json_path.stem + ".md")
#             with open(req_json_md, "w") as req_json_md_file:
#                 req_json_md_file.write(md_file_content)
#
#
# for child in requirements_src_path.iterdir():
#     if child.is_dir():
#         print(child)
#         handle_requirement_src_path(child)
