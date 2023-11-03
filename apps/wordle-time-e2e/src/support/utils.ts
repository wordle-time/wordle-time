export function getScreenshotPath(
  requirementNr: string,
  groupName: string,
  testname: string
) {
  return `/${requirementNr}/${groupName} -- ${testname}`;
}
