# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
I don't see multiple implementation strategies, however I also note this file says there are 3 questions when there are only 2. Is it posisble some of this has changed over time?
My personal preference to to use core JPA and Spring-JPA tools to manage database manipulation. It works well, is well documented, and is well understood by developers. 
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
My personal preference is that all code is commited to version control, which includes code that may have originally been automatically generated. While it exists in the /target folder it always has the possibility of being different on each developers machine, potentially leading to problems.
Likewise my preference is to hand-code API specifications rather than rely on tools to automatically generate artefacts. For the same reason, it means there is no ambiguity in the code that is being run, and we are not dependanton the tools which could change things without our explicit knowledge or intent.
```