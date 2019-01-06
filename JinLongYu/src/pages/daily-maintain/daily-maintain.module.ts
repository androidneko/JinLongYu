import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { DailyMaintainPage } from './daily-maintain';

@NgModule({
  declarations: [
    DailyMaintainPage,
  ],
  imports: [
    IonicPageModule.forChild(DailyMaintainPage),
  ],
})
export class DailyMaintainPageModule {}
