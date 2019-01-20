import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { FumigationPage } from './fumigation';
import { MultiPickerModule } from '../../../node_modules/ion-multi-picker';

@NgModule({
  declarations: [
    FumigationPage,
  ],
  imports: [
    IonicPageModule.forChild(FumigationPage),
    MultiPickerModule
  ],
})
export class FumigationPageModule {}
