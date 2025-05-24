import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LeftSidebarComponent } from "./components/left-sidebar/left-sidebar.component";
import { MainComponent } from "./components/main/main.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LeftSidebarComponent, MainComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'front-end';
}
