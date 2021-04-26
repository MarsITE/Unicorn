import { Project } from './project';
import { Skill } from './skill';

export interface UserInfo {
  userInfoId: string;
  firstName: string;
  lastName: string;
  phone: string;
  linkToSocialNetwork: string;
  birthDate: string;
  showInfo: boolean;
  workStatus: string;
  imageUrl: string;
  skills: Skill[];
  projects: Project[];
}
