
/*
 * constants
 */

export const CHANGE_WIP = 'CHANGE_WIP';

/*
 * action creators
 */

export function changeWip(phase, wipLimit) {
  return { type: CHANGE_WIP, phase, wipLimit };
}
