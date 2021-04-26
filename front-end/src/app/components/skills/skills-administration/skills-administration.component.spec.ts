import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SkillsAdministrationComponent } from './skills-administration.component';

describe('SkillsAdministrationComponent', () => {
  let component: SkillsAdministrationComponent;
  let fixture: ComponentFixture<SkillsAdministrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SkillsAdministrationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SkillsAdministrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
