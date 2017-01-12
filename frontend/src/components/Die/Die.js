import React, { PropTypes } from 'react';
import "./Die.scss";

const Die = ({castNumber, addedClass, dieStyle}) => {
  const dieClass = "die "+addedClass;
  return (
    <div className={dieClass} style={dieStyle}>{castNumber}</div>
  )
};

Die.propTypes = {
  castNumber: PropTypes.number.isRequired,
  addedClass: PropTypes.string.isRequired,
  dieStyle: PropTypes.object.isRequired
};

export default Die;
