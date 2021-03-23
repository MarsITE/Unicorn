package com.academy.workSearch.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

    @NotNull
    @Email
    String email;

    @NotNull
    String subject;

    @NotNull
    String message;
}
