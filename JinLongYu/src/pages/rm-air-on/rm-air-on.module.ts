import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RmAirOnPage } from './rm-air-on';
import { MultiPickerModule } from '../../../node_modules/ion-multi-picker';

@NgModule({
  declarations: [
    RmAirOnPage,
  ],
  imports: [
    IonicPageModule.forChild(RmAirOnPage),
    MultiPickerModule
  ],
})
export class RmAirOnPageModule {}
