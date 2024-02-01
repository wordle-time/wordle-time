from pathlib import Path
import subprocess


def print_md_to_pdf():
    def md_to_pdf(md_files_root_path: Path):
        """Transforms markdown files into pdf files and deletes markdown files

        Args:
            md_files_root_path (Path): root directory in which to look for markdown files (recursively)
        """

        script_dir = Path(__file__).absolute().parent
        disable_float_path = script_dir.joinpath("disable_float.tex")

        md_files = sorted(md_files_root_path.glob('**/*.md'))
        if md_files:
            for index, src_str in enumerate(md_files):
                src_path = Path(src_str)
                dest_path = src_path.with_suffix(".pdf")
                print(f"({index+1}/{len(md_files)}): Converting {src_path.relative_to(md_files_root_path)} -> {dest_path.relative_to(md_files_root_path)}")
                subprocess.run(["/usr/bin/pandoc", "-H", disable_float_path, src_path, "--from=markdown+rebase_relative_paths", "-o", dest_path])
                src_path.unlink()

    docs_path = Path.cwd().joinpath("docs")
    requirements_output_path = docs_path.joinpath(".requirements")
    other_output_path = docs_path.joinpath("./.other")

    md_to_pdf(requirements_output_path)
    md_to_pdf(other_output_path)


if __name__ == '__main__':
    print_md_to_pdf()
