import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ApplyPage } from './apply';
import { MultiPickerModule } from 'ion-multi-picker';

@NgModule({
  declarations: [
    ApplyPage,
  ],
  imports: [
    IonicPageModule.forChild(ApplyPage),
    MultiPickerModule
  ],
})
export class ApplyPageModule {}
