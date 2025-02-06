export class Projekt {
    name: string;
    description: string;
    redirectLink : string;
    redirectLinkSpecification : string;
    
    
    constructor(name: string, description: string, redirectLink: string, redirectLinkSpecification: string) {
      this.name = name;
      this.description = description;
      this.redirectLink = redirectLink;
      this.redirectLinkSpecification = redirectLinkSpecification;
    }
  }
