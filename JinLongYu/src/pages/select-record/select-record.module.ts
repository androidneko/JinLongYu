import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SelectRecordPage } from './select-record';
import { ComponentsModule } from '../../components/components.module';

@NgModule({
  declarations: [
    SelectRecordPage,
  ],
  imports: [
    IonicPageModule.forChild(SelectRecordPage),
    ComponentsModule
  ],
})
export class SelectRecordPageModule {}
