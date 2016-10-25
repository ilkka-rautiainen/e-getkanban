import React, { PropTypes } from 'react';

const CardInfoItem = ({ title, value }) => {
    return <div className="card-info-item">
        <span>{title}</span>
        <div className="info-oval">
            <span>{value}</span>
        </div>
    </div>
};

CardInfoItem.propTypes = {
    title: PropTypes.string.isRequired,
    value: PropTypes.number
};

export default CardInfoItem;


