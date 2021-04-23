import { Project } from './project';

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
  skills: string[];
  projects: Project[];
}
