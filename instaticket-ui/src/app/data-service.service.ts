import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataServiceService {
  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  login(user) {
    return this.http.post(this.baseUrl + 'auth/login', user)
  }

}
