import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { UnsupportedPage } from './unsupported';
import { ComponentsModule } from '../../components/components.module';

@NgModule({
  declarations: [
    UnsupportedPage,
  ],
  imports: [
    IonicPageModule.forChild(UnsupportedPage),
    ComponentsModule
  ],
})
export class UnsupportedPageModule {}
