export interface Requirement {
  id: string;
  title: string;
  reference: string;
  description: string;
  impact: string;
  criteria: string[];
  testCases: TestCase[];
  actPic: string;
  seqPic: string;
}

export interface TestCase {
  name: string;
  requirement: string;
  action: string;
  expectation: string;
  testPic: string;
}

export interface Requirements {
  requirements: Requirement[];
}
