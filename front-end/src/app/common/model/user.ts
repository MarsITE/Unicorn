import { UserInfo } from './user-info';

export interface User {
  email: string;
  userRole: string[];
  accountStatus: string;
  userInfo: UserInfo;
}