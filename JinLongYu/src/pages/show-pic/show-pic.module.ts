import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ShowPicPage } from './show-pic';

@NgModule({
  declarations: [
    ShowPicPage,
  ],
  imports: [
    IonicPageModule.forChild(ShowPicPage),
  ],
})
export class ShowPicPageModule {}
