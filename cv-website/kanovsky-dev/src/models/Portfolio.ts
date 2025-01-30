import { Projekt } from "./Projekt";

export class Portfolio {
    title: string;
    projects: Projekt[];
    redirectButtonText : string;
  
    constructor(title: string, projects: Projekt[],redirectButtonText: string) {
      this.title = title;
      this.projects = projects;
      this.redirectButtonText = redirectButtonText;
    }
  }
