import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RmCoolingOnPage } from './rm-cooling-on';
import { MultiPickerModule } from '../../../node_modules/ion-multi-picker';

@NgModule({
  declarations: [
    RmCoolingOnPage,
  ],
  imports: [
    IonicPageModule.forChild(RmCoolingOnPage),
    MultiPickerModule
  ],
})
export class RmCoolingOnPageModule {}
