from pathlib import Path
import shutil
import json
from urllib.parse import quote


def export_requirements():
    docs_path = Path.cwd().joinpath("docs")
    requirements_output_path = docs_path.joinpath(".requirements")
    requirements_output_path.mkdir(exist_ok=True)

    requirements_src_path = docs_path.joinpath("requirements-src")

    def testcase_dict_to_md(testcase_dict: dict[str, str]) -> str:
        test_case_lines = [
            "",
            f"### {testcase_dict['name']}",
            "",
            f"**Vorbedingung:** {testcase_dict['requirement']}",
            "",
            f"**Aktion:** {testcase_dict['action']}",
            "",
            f"**Erwartetes Ergebnis:** {testcase_dict['expectation']}",
            "",
            f"![{testcase_dict['name']}]({quote(testcase_dict['testPic'])})"
        ]

        return "\n".join(test_case_lines)

    def requirement_to_md(json_dict: dict[str, any], add_act_diagram: bool, add_seq_diagram: bool) -> str:
        desc_lines = [
            f"# {json_dict['id']} - {json_dict['title']}",
            "",
            json_dict['description'],
            "",
            f"[GitHub-Issue]({json_dict['reference']})",
            "",
            "## Auswirkung",
            "",
            json_dict['impact'],
            "",
            "## Akzeptanzkriterien",
            "",
            "\n".join(map(lambda x: f"- {x}", json_dict['criteria'])),
        ]

        if add_act_diagram:
            desc_lines.extend([
                "",
                "## Aktivitätsdiagramm",
                "",
                f"![Aktivitätsdiagramm]({quote(json_dict['id'] + '_act.png')})"
            ])

        if add_seq_diagram:
            desc_lines.extend([
                "",
                "## Sequenzdiagramm",
                "",
                f"![Sequenzdiagramm]({quote(json_dict['id'] + '_seq.png')})"
            ])

        full_text = "\n".join(desc_lines)

        if json_dict['testCases']:
            test_cases_lines = [
                "\n",
                "## Testfälle"
            ]

            for testcase_dict in json_dict['testCases']:
                test_cases_lines.append(testcase_dict_to_md(testcase_dict))

            full_text += "\n".join(test_cases_lines)

        return full_text

    def handle_requirement_src_path(src_path: Path):
        dest_path = requirements_output_path.joinpath(src_path.name)
        dest_path.mkdir(exist_ok=True)

        # Copy PNG/Json
        for png_path in src_path.glob("*.png"):
            shutil.copy2(png_path, dest_path.joinpath(png_path.name))
        for json_path in src_path.glob("*.json"):
            shutil.copy2(json_path, dest_path.joinpath(json_path.name))

        add_act_diagram = any(True for _ in src_path.glob('*-act.plantuml'))
        add_seq_diagram = any(True for _ in src_path.glob('*-seq.plantuml'))

        # Generate F**-req.md
        req_json_path = src_path.joinpath(f"{src_path.name}-req.json")
        if req_json_path.exists:
            with open(req_json_path, "r") as json_file:
                data = json.load(json_file)
                md_file_content = requirement_to_md(data, add_act_diagram, add_seq_diagram)
                req_json_md = dest_path.joinpath(req_json_path.stem + ".md")
                with open(req_json_md, "w") as req_json_md_file:
                    req_json_md_file.write(md_file_content)

    for child in requirements_src_path.iterdir():
        if child.is_dir():
            print(child)
            handle_requirement_src_path(child)


if __name__ == '__main__':
    export_requirements()
