export class Projekt {
    name: string;
    description: string;
    redirectLink : string
    
    
    constructor(name: string, description: string, redirectLink: string) {
      this.name = name;
      this.description = description;
      this.redirectLink = redirectLink;
    }
  }
