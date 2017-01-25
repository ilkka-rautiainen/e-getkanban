import React, { PropTypes } from 'react';
import "./Message.scss";

const Message = ({message}) => {
  return (
    <div className="message">{message}</div>
  )
};

Message.propTypes = {
  message: PropTypes.arrayOf(PropTypes.object).isRequired,
};

export default Message;
