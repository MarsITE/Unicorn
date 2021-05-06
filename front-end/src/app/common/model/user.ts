import { UserInfo } from './user-info';

export interface User {
  userId: string;
  email: string;
  roles: string[];
  userInfo: UserInfo;
}
