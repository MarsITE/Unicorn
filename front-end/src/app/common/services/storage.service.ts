import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }


  saveValue(name: string, value: string): void {
    sessionStorage.setItem(name, value);
  }

  getValue(name: string): string {
    return sessionStorage.getItem(name);
  }

  remove(name: string): void {
    sessionStorage.removeItem(name);
  }
}
