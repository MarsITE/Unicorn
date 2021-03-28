import { UserInfo } from './user-info';

export interface User {
  email: string;
  roles: string[];
  userInfo: UserInfo;
}
