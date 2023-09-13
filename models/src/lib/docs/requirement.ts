export interface IRequirement {
  id: string;
  title: string;
  reference: string;
  description: string;
  impact: string;
  criteria: string[];
  testCases: ITestCase[];
  actPic: string;
  seqPic: string;
}

export interface ITestCase {
  name: string;
  requirement: string;
  action: string;
  expectation: string;
  testPic: string;
}

export interface Requirements {
  requirements: IRequirement[];
}
