import { AuthenticationService } from '../services/authentication.service';

export function appInitializer(authenticationService: AuthenticationService): any {
  return () => new Promise(resolve => {
      authenticationService.refreshToken()
        .subscribe()
        .add(resolve);
  });
}
