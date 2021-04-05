import { User } from "./user";

export class Project {
    id: string;
    name: string;
    description: string;
    projectStatus: string;
    creationDate: string;
    owner: User;
    skills: string[];
  }
