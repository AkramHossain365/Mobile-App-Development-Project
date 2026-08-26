# Fix Application Submission Failure

The user reports that the "Submit Application" button is not working and shows a "failed to submit" message. This indicates that `database.insertApplication` is returning `-1`.

## User Review Required

> [!IMPORTANT]
> I will be upgrading the database version and improving error logging to identify why the insertion is failing. I will also use `insertOrThrow` to catch and display specific SQL errors if they occur.

## Proposed Changes

### Java Source Code

#### [MODIFY] [ApplicationDatabaseHelper.java](file:///D:/Android Studio  Projects/New Hall Management Project/app/src/main/java/com/example/smarthallmanagement/ApplicationDatabaseHelper.java)

- Bump `DATABASE_VERSION` to `2` to ensure the `applications` table is created correctly if there was a previous schema mismatch.
- Update `insertApplication` to use `insertOrThrow` and wrap it in a `try-catch` block.
- Add a new method or modify the existing one to return a result object or throw a custom exception with the error message.
- For now, I'll update it to throw the exception so `ApplicationFormActivity` can catch it and show the error.

#### [MODIFY] [ApplicationFormActivity.java](file:///D:/Android Studio  Projects/New Hall Management Project/app/src/main/java/com/example/smarthallmanagement/ApplicationFormActivity.java)

- Wrap the call to `database.insertApplication` in a `try-catch` block.
- In the `catch` block, show a `Toast` with the specific error message to help the user (and us) understand why it failed.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure the project compiles.

### Manual Verification
- The user will try to submit an application again. If it fails, they will see a more descriptive Toast message instead of just "Failed to submit application".
