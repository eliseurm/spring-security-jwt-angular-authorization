import {Papel} from "./papel";

export class User {
  email!: string;
  roles: string[] = [];
  avatarUrl: string | undefined;
  token: string | undefined;

  constructor(email: string, roles: string[] | Papel[], avatarUrl?: string, token?: string) {
    this.email = email;
    this.setRoles(roles);
    this.avatarUrl = avatarUrl;
    this.token = token;
  }

  setRoles(roles: any) {
    if (roles && roles.length > 0) {
      this.roles = [];
      for (let role of roles) {
        if (role instanceof Papel || role.papel) {
          this.roles.push(role.papel.toString());
        }
        else {
          this.roles.push(role);
        }
      }
    }
  }

}

