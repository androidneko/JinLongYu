import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { FumitationRecordsPage } from './fumitation-records';

@NgModule({
  declarations: [
    FumitationRecordsPage,
  ],
  imports: [
    IonicPageModule.forChild(FumitationRecordsPage),
  ],
})
export class FumitationRecordsPageModule {}
